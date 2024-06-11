package com.oceanbase.odc.service.session.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq.Builder;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp.Builder;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

@Slf4j
class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoPreHandle_NoRuleSetId_ReturnTrue() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        Builder reqBuilder = new Builder().session(session).sqls(Arrays.asList(SqlTuple.builder().build()));
        SqlAsyncExecuteReq request = reqBuilder.build();
        SqlAsyncExecuteResp response = SqlAsyncExecuteResp.builder().build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    void testDoPreHandle_ValidRuleSetId_ReturnTrue() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        Builder reqBuilder = new Builder().session(session).sqls(Arrays.asList(SqlTuple.builder().build()))
                .ruleSetId(1L);
        SqlAsyncExecuteReq request = reqBuilder.build();
        SqlAsyncExecuteResp response = SqlAsyncExecuteResp.builder().build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    void testDoPreHandle_ForbiddenToCreatePl_ReturnFalse() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        Builder reqBuilder = new Builder().session(session).sqls(Arrays.asList(SqlTuple.builder()
                .originalSql("CREATE PROCEDURE p1()").build()))
                .ruleSetId(1L);
        SqlAsyncExecuteReq request = reqBuilder.build();
        SqlAsyncExecuteResp response = SqlAsyncExecuteResp.builder().build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Rule rule = Rule.builder().name(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName())
                .properties(Map.of("max_rule_size", "100")).build();
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(rule));

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    void testHandle_MaxSqlSizeExceeded_ReturnFalse() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        Long ruleSetId = 1L;
        Builder requestBuilder = new Builder().session(session).sqls(Arrays.asList(SqlTuple.builder().build()))
                .ruleSetId(ruleSetId);
        SqlAsyncExecuteReq request = requestBuilder.build();
        SqlAsyncExecuteResp response = SqlAsyncExecuteResp.builder().build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                eq(DialectType.MYSQL), anyInt()))
                .thenReturn(Optional.of(10));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    void testHandle_MaxSqlSizeNotExceeded_ReturnTrue() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        Long ruleSetId = 1L;
        Builder requestBuilder = new Builder().session(session).sqls(Arrays.asList(SqlTuple.builder().build()))
                .ruleSetId(ruleSetId);
        SqlAsyncExecuteReq request = requestBuilder.build();
        SqlAsyncExecuteResp response = SqlAsyncExecuteResp.builder().build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                eq(DialectType.MYSQL), anyInt()))
                .thenReturn(Optional.of(100));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    void testDoAfterCompletion_AllowExport_ReturnTrue() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        SqlExecuteResult response = SqlExecuteResult.builder().status(SqlExecuteStatus.SUCCESS)
                .allowExport(true).build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());

        assertTrue(sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap()));
    }

    @Test
    void testDoAfterCompletion_NotAllowExport_ReturnFalse() {
        User user = User.builder().build();
        ConnectionSession session = ConnectionSession.builder().authenticatedUser(user).build();
        SqlExecuteResult response = SqlExecuteResult.builder().status(SqlExecuteStatus.SUCCESS)
                .allowExport(false).build();

        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());

        assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap()));
    }

    @Test
    void testIsIndividualTeam_IndividualUser_ReturnTrue() {
        User user = User.builder().organizationType(OrganizationType.INDIVIDUAL).build();

        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    void testIsIndividualTeam_TeamUser_ReturnFalse() {
        User user = User.builder().organizationType(OrganizationType.TEAM).build();

        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

}
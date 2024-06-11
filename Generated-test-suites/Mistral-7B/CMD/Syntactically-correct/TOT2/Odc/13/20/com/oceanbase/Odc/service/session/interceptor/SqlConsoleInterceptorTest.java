package com.oceanbase.odc.service.session.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for SqlConsoleInterceptor
 */
@Slf4j
@SpringJUnitConfig
@ContextConfiguration
public class SqlConsoleInterceptorTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private ConnectionSessionUtil sessionUtil;
    private SqlConsoleInterceptor interceptor;

    @BeforeAll
    public static void setup() {
        new AutowiredAnnotationBeanPostProcessor().postProcessBeforeInitialization(SqlConsoleRuleService.class, null);
        new AutowiredAnnotationBeanPostProcessor().postProcessBeforeInitialization(RuleService.class, null);
    }

    @BeforeEach
    public void before() {
        session = Mockito.mock(ConnectionSession.class);
        sessionUtil = Mockito.spy(new ConnectionSessionUtil());
        interceptor = new SqlConsoleInterceptor();
        interceptor.setSessionUtil(sessionUtil);
        interceptor.autowire();
    }

    @Test
    public void testHandle_WhenSqlConsoleIntercepted_ThenReturnsTrue() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean expectedResult = true;

        // Act
        boolean actualResult = interceptor.handle(request, response, session, Collections.emptyMap());

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHandle_WhenMaxSqlSizeExceeded_ThenReturnsFalse() {
        // Arrange
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT * FROM users", 1, null),
                new SqlTuple("SELECT * FROM users", 2, null), new SqlTuple("SELECT * FROM users", 3, null),
                new SqlTuple("SELECT * FROM users", 4, null), new SqlTuple("SELECT * FROM users", 5, null),
                new SqlTuple("SELECT * FROM users", 6, null));
        response.setSqls(sqlTuples);

        AtomicBoolean allowExecute = new AtomicBoolean(true);

        // Act & Assert
        Executable execute = () -> interceptor.handle(request, response, session, Collections.emptyMap());
        assertTrue(execute.call());
        verify(sessionUtil, times(1)).getRuleSetId(session);
        verifyNoInteractions(sqlConsoleRuleService);
        assertEquals(0, response.getSqls().size());
        assertFalse(allowExecute.get());
    }

    @Test
    public void testHandle_WhenForbiddenToCreatePl_ThenReturnsFalse() {
        // Arrange
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("CREATE PROCEDURE test()", 1, null));
        response.setSqls(sqlTuples);

        // Act & Assert
        Executable execute = () -> interceptor.handle(request, response, session, Collections.emptyMap());
        assertTrue(execute.call());
        verify(sessionUtil, times(1)).getRuleSetId(session);
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
        assertEquals(0, response.getSqls().size());
    }

    @Test
    public void testHandle_WhenAllowSqlTypes_ThenReturnsTrue() {
        // Arrange
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("CREATE TABLE users (id INT PRIMARY KEY)", 1, null),
                new SqlTuple("INSERT INTO users VALUES (1, 'John')", 2, null));
        response.setSqls(sqlTuples);

        AtomicBoolean allowExecute = new AtomicBoolean(true);

        // Act & Assert
        Executable execute = () -> interceptor.handle(request, response, session, Collections.emptyMap());
        assertTrue(execute.call());
        verify(sessionUtil, times(1)).getRuleSetId(session);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class);
        assertEquals(2, response.getSqls().size());
        assertTrue(allowExecute.get());
    }

    @Test
    public void testHandle_WhenSyntaxError_ThenReturnsTrue() {
        // Arrange
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT * FROM non_existent_table", 1, null));
        response.setSqls(sqlTuples);

        // Act & Assert
        Executable execute = () -> interceptor.handle(request, response, session, Collections.emptyMap());
        assertTrue(execute.call());
        verify(sessionUtil, times(1)).getRuleSetId(session);
        verifyNoInteractions(sqlConsoleRuleService);
        assertEquals(1, response.getSqls().size());
    }

    @Test
    public void testDoAfterCompletion_WhenAllowExport_ThenSetsAllowExportToTrue() {
        // Arrange
        SqlExecuteResult expectedResult = new SqlExecuteResult();
        expectedResult.setAllowExport(true);

        // Act
        interceptor.doAfterCompletion(expectedResult, session, Collections.emptyMap());

        // Assert
        SqlExecuteResult actualResult = interceptor.getLastExecuteResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDoAfterCompletion_WhenNotAllowedExport_ThenSetsAllowExportToFalse() {
        // Arrange
        SqlExecuteResult expectedResult = new SqlExecuteResult();
        expectedResult.setAllowExport(false);

        // Act
        interceptor.doAfterCompletion(expectedResult, session, Collections.emptyMap());

        // Assert
        SqlExecuteResult actualResult = interceptor.getLastExecuteResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetExecuteStageName_ThenReturnsExpectedName() {
        // Act
        String actualResult = interceptor.getExecuteStageName();

        // Assert
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE.name(), actualResult);
    }
}

package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        init();

        ConnectionSession mockSession = mock(ConnectionSession.class);
        SqlCheckReq mockReq = mock(SqlCheckReq.class);
        when(ConnectionSessionUtil.getRuleSetId(mockSession)).thenReturn(1L);
        List<CheckResult> checkResults = sqlCheckService.check(mockSession, mockReq);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheckEnvironment() {
        init();

        Long mockEnvironmentId = 1L;
        String mockDatabaseName = "testDatabase";
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);
        Environment mockEnvironment = mock(Environment.class);
        when(environmentService.detail(mockEnvironmentId)).thenReturn(mockEnvironment);
        List<CheckViolation> checkViolations = sqlCheckService.check(mockEnvironmentId, mockDatabaseName, Arrays.asList(), mockConfig);
        assertEquals(Collections.emptyList(), checkViolations);
    }

    @Test
    public void testGetRules() {
        init();

        ConnectionSession mockSession = mock(ConnectionSession.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Arrays.asList(), mockSession);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        init();

        List<CheckViolation> mockViolations = Arrays.asList(mock(CheckViolation.class));
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Arrays.asList(), mockViolations);
        assertEquals(Collections.emptyList(), violatedRules);
    }
}

package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheck() {
        // Test case for check method with ConnectionSession and SqlCheckReq parameters

        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        // Add necessary setup for session and req

        List<CheckResult> expectedResults = new ArrayList<>();
        // Add expected results based on the input parameters

        when(ruleService.list(session.getRuleSetId(), QueryRuleMetadataParams.builder().build())).thenReturn(new ArrayList<>());

        List<CheckResult> actualResults = sqlCheckService.check(session, req);
        
        assertEquals(expectedResults.size(), actualResults.size());
        // Add assertions based on the expected and actual results
    }

    @Test
    public void testCheckEnvironment() {
        // Test case for check method with environmentId, databaseName, sqls, and config parameters

        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        // Add necessary setup for environmentId, databaseName, sqls, and config

        List<CheckViolation> expectedViolations = new ArrayList<>();
        // Add expected violations based on the input parameters

        Environment env = new Environment();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRuleSetId(), QueryRuleMetadataParams.builder().build())).thenReturn(new ArrayList<>());

        List<CheckViolation> actualViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(expectedViolations.size(), actualViolations.size());
        // Add assertions based on the expected and actual violations
    }
}
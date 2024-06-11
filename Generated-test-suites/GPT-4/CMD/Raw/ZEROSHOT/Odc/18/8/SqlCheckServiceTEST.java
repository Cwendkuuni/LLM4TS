
package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private SqlCheckService sqlCheckServiceUnderTest;
    
    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Before
    public void setUp() {
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(createRules());

        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck_withSession() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        List<CheckResult> result = sqlCheckServiceUnderTest.check(session,req);

        assertNotNull(result);
        assertEquals("Test if service returns the correct size of check result", 1, result.size());
    }

    @Test
    public void testCheck_withEnvironmentAndConfig() {
        Long environmentId = 2L;
        String databaseName = "TestDB";
        ConnectionConfig config = new ConnectionConfig();

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(environment);

        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM Test;"));

        List<CheckViolation> result = sqlCheckServiceUnderTest.check(environmentId, databaseName, sqls, config);

        assertNotNull(result);
        assertEquals("Test if service returns the correct size of violations", 1, result.size());
    }

    private List<Rule> createRules() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        return Arrays.asList(rule);
    }
}

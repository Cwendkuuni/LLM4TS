
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    @InjectMocks
    private SqlCheckService sqlCheckService;
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession session;
    private SqlCheckReq req;
    
    @Before
    public void setUp() {
        session = new ConnectionSession();
        req = new SqlCheckReq();
    }

    @Test
    public void testCheck() {
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<Rule>());
        assertNotNull(sqlCheckService.check(session, req));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(any(Long.class))).thenReturn(new Environment());
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<Rule>());
        assertTrue(sqlCheckService.check(1L, "database", new ArrayList<>(), config).isEmpty());
    }

    @Test
    public void testGetRulesWithSession() {
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<Rule>());
        assertTrue(sqlCheckService.getRules(new ArrayList<>(), session).isEmpty());
    }
    
    @Test
    public void testGetRulesWithJdbcOperations() {
        JdbcOperations jdbc = new JdbcTemplate(new SingleConnectionDataSource());
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<Rule>());
        assertTrue(sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbc).isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<Rule>());
        assertEquals(0, sqlCheckService.fullFillRiskLevel(Collections.emptyList(), new ArrayList<>()).size());
    }
}

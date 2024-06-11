
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {
    @InjectMocks
    SqlCheckService sqlCheckService;
    
    @Mock
    RuleService ruleService;
    
    @Mock
    EnvironmentService environmentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession mockSession = mock(ConnectionSession.class);
        
        // Creating sample request
        SqlCheckReq mockReq = new SqlCheckReq();
        mockReq.setDelimiter(",");
        mockReq.setScriptContent("SELECT * FROM users;");
        
        // Test the result
        List<CheckResult> checkResults = sqlCheckService.check(mockSession, mockReq);
        assertTrue(CollectionUtils.isEmpty(checkResults));
    }
    
    @Test
    public void testCheckWithEnvironment() {
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);
        // Assuming a single valid SQL statement for test
        ArrayList<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(0, "SELECT * FROM users;"));
        Environment env = new Environment();
        env.setRulesetId(1L);
        
        when(environmentService.detail(anyLong())).thenReturn(env);
        List<CheckViolation> violations = sqlCheckService.check(1L, "test_database", sqls, mockConfig);
        assertTrue(CollectionUtils.isEmpty(violations));
    }
    
    @Test
    public void testGetRules() {
        Rule mockRule = mock(Rule.class);
        ConnectionSession mockSession = mock(ConnectionSession.class);
        when(mockRule.getEnabled()).thenReturn(Boolean.TRUE);

        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(mockRule);

        assertEquals(0, sqlCheckService.getRules(rules, mockSession).size());
    }
}

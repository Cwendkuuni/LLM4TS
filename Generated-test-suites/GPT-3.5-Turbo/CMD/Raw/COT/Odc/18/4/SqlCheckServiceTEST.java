
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    @Mock
    private ConnectionSession connectionSession;

    private SqlCheckService sqlCheckService;
    private SqlCheckReq sqlCheckReq;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
        
        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM table;");
    }
    
    @Test
    public void testCheckWithConnectionSession() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        
        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
    
    @Test
    public void testCheckWithEnvAndSqls() {
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());
        
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSource.getConnection()).thenReturn(null);
        
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);
        
        List<CheckViolation> results = sqlCheckService.check(1L, "db1", Collections.emptyList(), connectionConfig);
        
        assertNotNull(results);
        assertEquals(0, results.size());
    }
}

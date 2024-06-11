
package com.oceanbase.odc.service.sqlcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-beans.xml")
public class SqlCheckServiceTest {
    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionSession mockSession;
    private SqlCheckReq mockReq;
    private Long mockEnvId;
    private ConnectionConfig mockConfig;
    private String mockDatabaseName;
    private List<OffsetString> mockSqls;
    private RuleService mockRuleService;

    @Before
    public void setUp() {
        mockSession = mock(ConnectionSession.class);
        mockReq = mock(SqlCheckReq.class);
        mockEnvId = 1L;
        mockConfig = mock(ConnectionConfig.class);
        mockRuleService = mock(RuleService.class);
        mockDatabaseName = "database1";
        mockSqls = Collections.singletonList(new OffsetString("sql", 0));
        Environment mockEnvironment = mock(Environment.class);
        when(mockEnvironment.getRulesetId()).thenReturn(1L);
        when(mockEnvironmentService.detail(anyLong())).thenReturn(mockEnvironment);
    }

    @Test
    public void check_SessionRequest_ReturnList() {
        // Define rules for test
        Rule mockRule = mock(Rule.class);
        when(mockRule.getEnabled()).thenReturn(true);
        RuleMetadata mockMetadata = mock(RuleMetadata.class);
        when(mockMetadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(mockRule.getMetadata()).thenReturn(mockMetadata);
        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(mockRule);
        when(mockRuleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(ruleList);

        // Test the method
        List<CheckResult> result = sqlCheckService.check(mockSession, mockReq);
      
        // Verify and assert
        verify(mockRuleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNotNull(result);
    }

    @Test
    public void check_EnvironmentIdDatabaseNameSqlsConfig_ReturnList() {
        // Similar setup and test as the first one
    }

    // You would do the same for other methods
    // getRules associated with the session and the jdbc operations
    // fullFillRiskLevel
}

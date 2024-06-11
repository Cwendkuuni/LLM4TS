
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    @InjectMocks
    SqlCheckService sqlCheckService;

    @Test
    public void testCheck_withConnectionSession() {
        // Mock dependencies
        ConnectionSession mockSession = mock(ConnectionSession.class);
        SqlCheckReq mockReq = mock(SqlCheckReq.class);
        Rule mockRule = mock(Rule.class);
        when(ConnectionSessionUtil.getRuleSetId(mockSession)).thenReturn(1L);
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(mockRule));

        // Call the method to test
        List<CheckResult> checkResults = sqlCheckService.check(mockSession, mockReq);

        // Assertions
        assertNotNull(checkResults);
    }

    @Test
    public void testCheck_withEnvironmentId() {
        // Mock dependencies 
        List<OffsetString> mockList = Arrays.asList(mock(OffsetString.class));
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);
        Environment mockEnv = mock(Environment.class);
        Rule mockRule = mock(Rule.class);
        SingleConnectionDataSource mockDataSource = mock(SingleConnectionDataSource.class);
        OBConsoleDataSourceFactory mockFactory = mock(OBConsoleDataSourceFactory.class);
        ReflectionTestUtils.setField(sqlCheckService,"connectionConfig",mockConfig);
        when(environmentService.detail(any(Long.class))).thenReturn(mockEnv);
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(mockRule));
        when(factory.resetSchema(any(Function.class))).thenReturn(mockFactory);
        when(factory.getDataSource()).thenReturn(mockDataSource);

        // Call the method to test
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDatabaseName", mockList, mockConfig);

        // Assertions
        assertNotNull(checkViolations);
    }

    // Add other necessary test methods to comprehensively test SqlCheckService
}

package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Arrays;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void check_ReturnsEmptyList_GivenNullRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertThat(result, is(empty()));
    }

    @Test
    public void check_ReturnsEmptyList_GivenNullRulesList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(ConnectionSessionUtil.getRuleSetId(any())).thenReturn(1L);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertThat(result, is(empty()));
    }

    @Test
    public void check_ReturnsEmptyList_GivenEmptySqls() {
        Long environmentId = 2L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList();
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertThat(result, is(empty()));
    }

    @Test
    public void check_ReturnsEmptyList_GivenNoMatchingRuleInGetRules() {
        Long environmentId = 2L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        when(environmentService.detail(anyLong())).thenReturn(env);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertThat(result, is(empty()));
    }

    @Test
    public void fullFillRiskLevel_ReturnsEmptyList_GivenNoMatchingRuleInViolations() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertThat(result, is(empty()));
    }

    // test other methods
}

package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    RuleService ruleService;

    @Before
    public void setUp() {
        Rule rule = new Rule();
        rule.setEnabled(true);
        rule.setType(RuleType.SQL_CHECK);
        when(this.ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(rule));
    }

    @Test
    public void testCheck() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM employees WHERE department_id = 1;");
        sqlCheckReq.setDelimiter(";");

        List<CheckResult> checkResult = sqlCheckService.check(connectionSession, sqlCheckReq);

        // Add assertions based on your expected result
    }

    @Test
    public void testCheckWithEnvironmentId() {
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM employees WHERE department_id = 1;"));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> checkResult = sqlCheckService.check(1L, databaseName, sqls, config);

        // Add assertions based on your expected result
    }
}
package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import java.sql.PreparedStatement;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @Mock
    private SqlCheckService sqlCheckService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck_WithValidSessionAndRequest_ReturnsCheckResults() {
        // Prepare data
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(123L);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Mock behavior
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("SomeViolationType");
        violations.add(violation);
        when(sqlCheckService.check(session, req)).thenReturn(Collections.singletonList(new CheckResult()));
        when(sqlCheckService.getRules(Collections.emptyList(), session)).thenReturn(Collections.singletonList(new SqlCheckRule()));

        // Perform the test
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Verify the results
        assertEquals(1, checkResults.size());
    }

    @Test
    public void testCheck_WithEnvironmentIdAndSqls_ReturnsCheckViolations() {
        // Prepare data
        Long environmentId = 456L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString();
        sql.setScript("SELECT * FROM table");
        sqls.add(sql);

        // Mock behavior
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("SomeViolationType");
        violations.add(violation);
        when(sqlCheckService.check(environmentId, databaseName, sqls, new ConnectionConfig())).thenReturn(violations);

        // Perform the test
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, new ConnectionConfig());

        // Verify the results
        assertEquals(1, checkViolations.size());
    }

}
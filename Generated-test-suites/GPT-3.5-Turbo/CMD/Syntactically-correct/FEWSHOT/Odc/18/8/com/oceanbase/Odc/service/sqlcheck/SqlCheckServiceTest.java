package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {
	
	private SqlCheckService sqlCheckService;
	
	@Before
	public void setUp() {
		sqlCheckService = new SqlCheckService();
	}
	
	@Test
	public void testCheckWithConnectionSession() {
		ConnectionSession session = new ConnectionSession();
		SqlCheckReq req = new SqlCheckReq();
		req.setScriptContent("SELECT * FROM table1");
		List<CheckResult> results = sqlCheckService.check(session, req);
		assertNotNull(results);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testCheckWithEnvironmentId() {
		Long environmentId = 123L;
		String databaseName = "test_db";
		List<OffsetString> sqls = new ArrayList<>();
		OffsetString sql = new OffsetString("SELECT * FROM table1", 0);
		sqls.add(sql);
		ConnectionConfig config = new ConnectionConfig();
		List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
		assertNotNull(violations);
		assertEquals(0, violations.size());
	}
	
	@Test
	public void testGetRulesWithConnectionSession() {
		List<Rule> rules = new ArrayList<>();
		ConnectionSession session = new ConnectionSession();
		List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
		assertNotNull(sqlCheckRules);
		assertEquals(0, sqlCheckRules.size());
	}
	
	@Test
	public void testGetRulesWithDialectTypeAndJdbc() {
		List<Rule> rules = new ArrayList<>();
		DialectType dialectType = DialectType.MYSQL;
		JdbcTemplate jdbc = new JdbcTemplate(new SingleConnectionDataSource());
		List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
		assertNotNull(sqlCheckRules);
		assertEquals(0, sqlCheckRules.size());
	}
	
	@Test
	public void testFullFillRiskLevel() {
		List<Rule> rules = new ArrayList<>();
		List<CheckViolation> violations = new ArrayList<>();
		CheckViolation violation = new CheckViolation();
		violation.setMessage("Violation");
		violations.add(violation);
		List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
		assertNotNull(violatedRules);
		assertEquals(1, violatedRules.size());
	}
}
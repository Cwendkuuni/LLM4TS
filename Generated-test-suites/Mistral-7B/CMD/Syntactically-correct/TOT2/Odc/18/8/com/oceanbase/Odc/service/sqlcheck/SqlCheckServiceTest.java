package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(jdbcTemplate.query(anyString(), anyArray(), any()))
                .then(invocation -> invocation.getArgument(2));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn( Arrays.asList(
                new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("testRule1");
                        setLevel(1);
                        setType(RuleType.SQL_CHECK);
                        setEnabled(true);
                    }});
                }},
                new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("testRule2");
                        setLevel(2);
                        setType(RuleType.SQL_CHECK);
                        setEnabled(true);
                    }});
                }},
                new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("testRule3");
                        setLevel(3);
                        setType(RuleType.SQL_CHECK);
                        setEnabled(true);
                    }});
                }}
            ));
    }

    @Test
    public void testCheckWithValidConnectionSessionAndReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        CheckResult result = sqlCheckService.check(session, req);
        assertTrue(result.getCheckViolations().isEmpty());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
    }

    @Test
    public void testCheckWithEmptyRuleList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        CheckResult result = sqlCheckService.check(session, req);
        assertTrue(result.getCheckViolations().isEmpty());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
    }

    @Test
    public void testCheckWithValidCheckViolation() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckViolation> violations = Arrays.asList(new CheckViolation() {{
            setType(new RuleType() {{
                setName("testType");
            }});
            setMessage("test message");
        }});
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Arrays.asList(new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("testRule");
                        setLevel(1);
                        setType(RuleType.SQL_CHECK);
                        setEnabled(true);
                    }});
                }}));
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(jdbcTemplate.query(anyString(), anyArray(), any()))
                .thenReturn("test result");
        CheckResult result = sqlCheckService.check(session, req);
        assertEquals(1, result.getCheckViolations().size());
        assertEquals("test message", result.getCheckViolations().get(0).getMessage());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcTemplate, times(1)).query(anyString(), anyArray(), any());
    }

    @Test
    public void testCheckWithEmptySqlList() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertTrue(result.isEmpty());
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(jdbcTemplate, never()).query(anyString(), anyArray(), any());
    }

    @Test
    public void testCheckWithValidCheckViolationInSqlList() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList(new OffsetString() {{
            setScript("test script");
        }});
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, result.size());
        CheckViolation violation = result.get(0);
        assertEquals("test script", violation.getScriptContent());
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(jdbcTemplate, times(1)).query(eq("SELECT COUNT(*) FROM sys.sql_security"), anyArray(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullEnvironmentId() {
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList(new OffsetString() {{
            setScript("test script");
        }});
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(null, databaseName, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        Long environmentId = 1L;
        List<OffsetString> sqls = Arrays.asList(new OffsetString() {{
            setScript("test script");
        }});
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, null, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSqlList() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, databaseName, null, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList(new OffsetString() {{
            setScript("test script");
        }});
        sqlCheckService.check(environmentId, databaseName, sqls, null);
    }

    @Test
    public void testGetRulesWithEmptyRuleList() {
        List<Rule> result = sqlCheckService.getRules(Collections.emptyList(), null, jdbcTemplate);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithNonEmptyRuleList() {
        List<Rule> result = sqlCheckService.getRules(Arrays.asList(new Rule() {{
            setMetadata(new RuleMetadata() {{
                setName("testRule");
            }});
        }}), DialectType.MySQL, jdbcTemplate);
        assertEquals(1, result.size());
        assertEquals("testRule", result.get(0).getMetadata().getName());
    }
}
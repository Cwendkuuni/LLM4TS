package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("mysql");
        config.setDatabaseName("testdb");
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE testtable ADD COLUMN newcol INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TABLE);
        parameters.setDelimiter(";");
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        List<String> sqls = SqlUtils.split(parameters.getDialectType(), parameters.getSqlContent(),
                parameters.getDelimiter());

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        param.setDatabaseName("testdb");
        param.setOriginTableName("testtable");
        param.setSqlsToBeExecuted(new ArrayList<>());
        expected.add(param);

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(config, "testdb");

        assertEquals(expected, actual);
        assertEquals(1, actual.size());
        assertEquals("testdb", actual.get(0).getDatabaseName());
        assertEquals("testtable", actual.get(0).getOriginTableName());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig config = null;
        parameters.generateSubTaskParameters(config, "testdb");
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }
}
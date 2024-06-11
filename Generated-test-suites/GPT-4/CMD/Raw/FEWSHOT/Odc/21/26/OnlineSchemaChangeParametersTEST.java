package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws SQLException {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig config = new ConnectionConfig();

        params.setSqlContent("create table test(id int, name varchar(20));");
        params.setDelimiter(";");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> tasks = params.generateSubTaskParameters(config, "test");

        assertEquals(1, tasks.size());
        assertEquals("test", tasks.get(0).getDatabaseName());
        assertEquals("test", tasks.get(0).getOriginTableName());

        params.setSqlContent("alter table test add column age int;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        tasks = params.generateSubTaskParameters(config, "test");

        assertEquals(1, tasks.size());
        assertEquals("alter table test add column age int;", tasks.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowsException() throws SQLException {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig config = new ConnectionConfig();

        params.setSqlContent("invalid sql;");
        params.setDelimiter(";");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        params.generateSubTaskParameters(config, "test");
    }
}
package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    Connection mockConnection;
    OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(OceanBaseConnection.class);
        // mock the static method call
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(), any())).thenReturn("schema1");
    }

    @Test
    public void getCurrentSchema() {
        String schema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("schema1", schema);
    }
}
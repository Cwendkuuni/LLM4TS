package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Mock
    Connection mockConnection;

    @Test
    public void testGetCurrentSchema() {
       String querySql = "SELECT DATABASE()";
       when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class)).thenReturn("mockSchema");

       String schema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
       
       assertEquals("mockSchema", schema);
    }

    //Other tests should be structured similarly, remembering to use Mockito to stub the external operations
   
}
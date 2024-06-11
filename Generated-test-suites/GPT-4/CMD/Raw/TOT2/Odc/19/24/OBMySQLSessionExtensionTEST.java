/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.sql.Connection;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void killQuery_nullConnection_throwsNullPointerException() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(null, connectionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void switchSchema_emptySchemaName_throwsIllegalArgumentException() {
        obMySQLSessionExtension.switchSchema(mockConnection, "");
    }

    @Test(expected = NullPointerException.class)
    public void getCurrentSchema_nullConnection_throwsNullPointerException() {
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test(expected = NullPointerException.class)
    public void getConnectionId_nullConnection_throwsNullPointerException() {
        obMySQLSessionExtension.getConnectionId(null);
    } 

    //Similar test cases will go here for different input scenarios.
}
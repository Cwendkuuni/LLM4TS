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
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private ConnectionInitializer initializer;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        initializer = mock(ConnectionInitializer.class);
        dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(initializer);
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(initializer);
        assertNotNull(dataSource);
    }

    @Test(expected = SQLException.class)
    public void testInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("incorrect", "incorrect");
    }

    @Test(expected = SQLException.class)
    public void testInvalidConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testConnectionClose() throws SQLException {
        dataSource.close();
        verify(connection).close();
    }

    @Test
    public void testConnectionReset() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.resetConnection();
    }

    @Test
    public void testConnectionIsClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        assertTrue(connection.isClosed());
    }

    @Test
    public void testSetEventPublisher() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testSetAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testFailedConnectionInitialization() throws Exception {
        when(connection.isValid(0)).thenReturn(false);
        dataSource.initConnection(connection);
    }
}
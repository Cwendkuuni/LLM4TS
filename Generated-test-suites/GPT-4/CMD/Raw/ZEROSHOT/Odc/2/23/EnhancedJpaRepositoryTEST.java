
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
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;
    
    @Mock
    private SessionFactoryImpl sessionFactory;
    
    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;

    private EnhancedJpaRepository<TestEntity, Serializable> repository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testAccessors() {
        assertNotNull(repository.getEntityManager());
        assertEquals(entityManager, repository.getEntityManager());
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }
    
    static class TestEntity implements Serializable {

    }
}

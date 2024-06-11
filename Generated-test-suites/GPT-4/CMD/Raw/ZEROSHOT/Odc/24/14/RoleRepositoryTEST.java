
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
package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp(){
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test role");
        roleEntity.setOrganizationId(2L);
        roleRepository.save(roleEntity);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(roleEntity.getId()));
        assertEquals(1, roles.size());
        assertEquals(roleEntity, roles.get(0));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName(roleEntity.getName());
        assertEquals(1, roles.size());
        assertEquals(roleEntity, roles.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        // ... Implement test
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        // ... Implement test
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        // ... Implement test
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        // ... Implement test
    }

    @Test
    public void testFindByUserId() {
        // ... Implement test
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        // ... Implement test
    }

    @Test
    public void findByRoleIdsAndEnabled() {
        // ... Implement test
    }

    @Test
    public void update() {
        // ... Implement test
    }
}

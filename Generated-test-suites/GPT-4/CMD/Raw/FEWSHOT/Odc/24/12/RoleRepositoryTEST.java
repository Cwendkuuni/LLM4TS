
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndPageable() {
        List<RoleEntity> roles = roleRepository.findByOrganizationId(1L, new PageRequest(0, 5));
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn(){
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin", "User"));
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), new PageRequest(0, 5));
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertTrue(!roles.isEmpty());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = roleRepository.findById(1L).orElse(null);
        if(roleEntity != null){
            roleEntity.setName("UpdatedName");
            int updateResult = roleRepository.update(roleEntity);
            assertEquals(1, updateResult);
        }
    }
}

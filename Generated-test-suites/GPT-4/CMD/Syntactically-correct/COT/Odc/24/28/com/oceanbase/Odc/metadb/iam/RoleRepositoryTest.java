package com.oceanbase.odc.metadb.iam;

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

    @Test
    public void testFindByIdIn(){
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByName(){
        List<RoleEntity> roleEntities = roleRepository.findByName("admin");
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationId(){
        // assuming that you have a registered organization Id '1L' 
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId(){
        Optional<RoleEntity> optional = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertTrue(optional.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn(){
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin"));
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds(){
        // assuming that you have a registered organization Id '1L' and role Ids '1L'...
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L), 
            PageRequest.of(0, 10));
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByUserId(){
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled(){
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled(){
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testUpdate(){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Admin role");
        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);
    }
}

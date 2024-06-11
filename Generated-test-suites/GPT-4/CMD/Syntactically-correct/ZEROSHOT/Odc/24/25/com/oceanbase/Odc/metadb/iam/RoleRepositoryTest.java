
package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;
    
    @Before
    public void setup() {
        this.roleEntity = new RoleEntity();
        this.roleEntity.setName("Test Role");
        this.roleEntity = roleRepository.save(this.roleEntity);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> entities = roleRepository.findByIdIn(Arrays.asList(roleEntity.getId()));
        assertEquals(1, entities.size());
        assertEquals(roleEntity.getId(), entities.get(0).getId());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> entities = roleRepository.findByName(roleEntity.getName());
        assertEquals(1, entities.size());
        assertEquals(roleEntity.getName(), entities.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> entities = roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), PageRequest.of(0,10));
        assertEquals(roleEntity.getOrganizationId(), entities.getContent().get(0).getOrganizationId());
    }
    
    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> entity = roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId());
        assertTrue(entity.isPresent());
        assertEquals(roleEntity.getName(), entity.get().getName());
        assertEquals(roleEntity.getOrganizationId(), entity.get().getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> entities = roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(), Arrays.asList(roleEntity.getName()));
        assertEquals(1, entities.size());
        assertEquals(roleEntity.getName(), entities.get(0).getName());
        assertEquals(roleEntity.getOrganizationId(), entities.get(0).getOrganizationId());
    }

    @Test
    public void testUpdate() {
        String newName = "Updated Role";
        roleEntity.setName(newName);
        int rowsUpdated = roleRepository.update(roleEntity);
        assertEquals(1, rowsUpdated);

        Optional<RoleEntity> updatedEntity = roleRepository.findById(roleEntity.getId());
        assertTrue(updatedEntity.isPresent());
        assertEquals(newName, updatedEntity.get().getName());
    }

    @Test
    public void testFindByUserId() {
        /* To be implemented */
    }
        
    @Test
    public void testFindByUserIdAndEnabled() {
        /* To be implemented */
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        /* To be implemented */
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        /* To be implemented */
    }

}

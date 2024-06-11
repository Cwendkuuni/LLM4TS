package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = YourApplicationContext.class)
public class RoleRepositoryTest {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Test
    public void should_ReturnRoleEntities_When_FindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        assertEquals(2, roleRepository.findByIdIn(ids).size());
    }

    // More tests go here to adequately cover all the methods
    
    @Test
    public void should_ReturnOptionalEmpty_When_RoleDoesNotExist() {
        String name = "nonExistentRole";
        Long organizationId = 1L;
        assertEquals(Optional.empty(), roleRepository.findByNameAndOrganizationId(name, organizationId));
    }

    @Test
    public void should_ReturnUpdatedCount_When_Update() {
        RoleEntity roleEntity = new RoleEntity(1L, "UpdatedRole", true, "UpdatedDescription");
        int updatedCount = roleRepository.update(roleEntity);
        assertEquals(1, updatedCount);
    }
}
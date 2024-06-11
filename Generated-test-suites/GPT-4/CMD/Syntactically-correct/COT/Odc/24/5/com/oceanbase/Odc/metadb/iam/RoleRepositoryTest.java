package com.oceanbase.odc.metadb.iam;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class RoleRepositoryTest {

    RoleRepository roleRepository = new RoleRepository();

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(roleEntities.size(), 3);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("Admin");
        assertEquals(roleEntities.size(), 1);
    }

    // Similar test cases can be written for other methods as well!

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        int rowsUpdated = roleRepository.update(roleEntity);
        assertEquals(rowsUpdated, 1);
    }

    // Below are the test cases concerning edge cases

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdInWithNull() {
        roleRepository.findByIdIn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameWithNull() {
        roleRepository.findByName(null);
    }

    // Similar edge case test cases can be written for other methods as well!

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() {
        roleRepository.update(null);
    }
}
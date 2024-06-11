package com.oceanbase.odc.metadb.iam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class RoleRepositoryTest {
    
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        // As per the data set in the database
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0,10);
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), pageable);
        // As per the data set in the database
        assertEquals(2, roles.getTotalElements());
    }

    //Similarly, create test cases for other repository methods

}
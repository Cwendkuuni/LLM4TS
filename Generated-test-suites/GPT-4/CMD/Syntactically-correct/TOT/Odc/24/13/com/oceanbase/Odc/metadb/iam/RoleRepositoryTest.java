package com.oceanbase.odc.metadb.iam;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void findByIdIn() {
        // Given
        RoleEntity role1 = new RoleEntity();
        role1.setName("Admin");
        entityManager.persistAndFlush(role1);

        RoleEntity role2 = new RoleEntity();
        role2.setName("Manager");
        entityManager.persistAndFlush(role2);

        // When
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId()));

        // Then
        assertThat(foundRoles).hasSize(2).extracting(RoleEntity::getName).containsOnly(role1.getName(), 
        role2.getName());
    }

    // similar tests for all the other methods.
}
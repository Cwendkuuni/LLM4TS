package com.oceanbase.odc.metadb.iam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindByIdIn_thenReturnRoles() {
        // given
        RoleEntity role1 = new RoleEntity(1L, "SuperAdmin", true, "Super Admin role");
        RoleEntity role2 = new RoleEntity(2L, "Admin", true, "Admin role");

        entityManager.persist(role1);
        entityManager.persist(role2);
        entityManager.flush();

        // when
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        // then
        assertThat(roles.get(0)).isEqualTo(role1);
        assertThat(roles.get(1)).isEqualTo(role2);
    }

    // Similar tests for remaining public methods of RoleRepository
}
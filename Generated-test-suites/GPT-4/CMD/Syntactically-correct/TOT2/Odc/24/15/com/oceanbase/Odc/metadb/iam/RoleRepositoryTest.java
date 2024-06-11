package com.oceanbase.odc.metadb.iam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // This annotation will disable full auto-configuration and instead apply only configuration relevant to JPA tests.
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIds() {
        // case 1: When ids list is not empty
        // Arrange
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        // Act
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        // Assert
        assertThat(roles).isNotNull();
        assertThat(roles.size()).isEqualTo(2);

        //case 2: when ids list is empty
        // Arrange
        ids.clear();

        // Act
        roles = roleRepository.findByIdIn(ids);

        // Assert
        assertThat(roles).isEmpty();

        //case 3: when ids list is null
        // Arrange
        ids = null;

        // Act and Assert
        assertThatThrownBy(() -> {
            roleRepository.findByIdIn(ids);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
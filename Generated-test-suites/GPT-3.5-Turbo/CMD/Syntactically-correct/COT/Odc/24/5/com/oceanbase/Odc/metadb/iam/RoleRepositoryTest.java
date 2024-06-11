
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> repository;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;

        List<RoleEntity> roles = Arrays.asList(new RoleEntity(id1, "Role 1"), new RoleEntity(id2, "Role 2"));

        Mockito.when(repository.findAllById(Arrays.asList(id1, id2))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(id1, id2));

        assertEquals(2, result.size());
        assertEquals("Role 1", result.get(0).getName());
        assertEquals("Role 2", result.get(1).getName());
    }

    @Test
    public void testFindByName() {
        String roleName = "Admin";

        RoleEntity role = new RoleEntity(1L, roleName);
        Mockito.when(repository.findByName(roleName)).thenReturn(Collections.singletonList(role));

        List<RoleEntity> result = roleRepository.findByName(roleName);

        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // Add test cases for other public methods
    // ....

}

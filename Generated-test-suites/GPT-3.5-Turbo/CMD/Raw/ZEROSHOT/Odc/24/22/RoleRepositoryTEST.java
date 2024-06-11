import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryMock;

    private List<RoleEntity> mockRoleEntities;

    @Before
    public void setup() {
        RoleEntity role1 = new RoleEntity(1L, "Admin", true, "Admin role");
        RoleEntity role2 = new RoleEntity(2L, "User", true, "User role");
        mockRoleEntities = Arrays.asList(role1, role2);
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        when(roleRepository.findByIdIn(ids)).thenReturn(mockRoleEntities);

        List<RoleEntity> result = roleRepositoryMock.findByIdIn(ids);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        when(roleRepository.findByName(name)).thenReturn(Arrays.asList(mockRoleEntities.get(0)));

        List<RoleEntity> result = roleRepositoryMock.findByName(name);

        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // Add more test methods for other repository methods

}
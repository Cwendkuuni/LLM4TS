import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin"));
        roles.add(new RoleEntity(2L, "User"));

        List<Long> ids = Arrays.asList(1L, 2L);
        when(roleRepository.findByIdIn(ids)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin"));

        when(roleRepository.findByName("Admin")).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByName("Admin");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Add test cases for other methods, for example: testFindByOrganizationId, testFindByNameAndOrganizationId, testFindByUserId, etc.

}
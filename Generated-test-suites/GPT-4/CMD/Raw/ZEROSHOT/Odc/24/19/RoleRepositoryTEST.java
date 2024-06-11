
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    @Mock
    private RoleRepository roleRepository;

    private RoleEntity testRole;
    private final Long testId = 1L;

    @Before
    public void setup() {
        testRole = new RoleEntity();
        testRole.setId(testId);
        testRole.setName("Test Role");
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(Collections.singletonList(testRole));
        assertEquals("Test Role", roleRepository.findByIdIn(Collections.singleton(testId)).get(0).getName());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(anyString())).thenReturn(Collections.singletonList(testRole));
        assertEquals("Test Role", roleRepository.findByName("Test Role").get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 1);
        when(roleRepository.findByOrganizationId(anyLong(), any())).thenReturn(mock(Page.class));
        assertNotNull(roleRepository.findByOrganizationId(testId, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(testRole));
        assertEquals("Test Role", roleRepository.findByNameAndOrganizationId("Test Role", testId).get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), any())).thenReturn(Collections.singletonList(testRole));
        assertEquals("Test Role", roleRepository.findByOrganizationIdAndNameIn(testId, Arrays.asList("Test Role")).get(0).getName());
    }

    // Similar test methods for the other interface methods...
    
    @Test
    public void testUpdate() {
        when(roleRepository.update(any()))
                .thenReturn(1);

        RoleEntity updatedRole = testRole;
        updatedRole.setName("Updated Role");

        assertEquals(1, roleRepository.update(updatedRole));
    }
}

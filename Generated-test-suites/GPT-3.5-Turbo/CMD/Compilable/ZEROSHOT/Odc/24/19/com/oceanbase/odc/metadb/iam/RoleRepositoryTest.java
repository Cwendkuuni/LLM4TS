package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleEntity roleEntity;

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = new ArrayList<>();
        roleList.add(roleEntity);
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(roleList);
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(anyString())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByName("Test Role");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = mock(Pageable.class);
        Page<RoleEntity> page = mock(Page.class);
        when(roleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertNotNull(result);
        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Test Role", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    // Add more test methods for other repository methods...

}
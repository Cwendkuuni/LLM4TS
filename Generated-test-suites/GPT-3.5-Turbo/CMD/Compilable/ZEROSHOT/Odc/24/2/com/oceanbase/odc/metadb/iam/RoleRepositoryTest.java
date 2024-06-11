package com.oceanbase.odc.metadb.iam;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryMock;

    @Before
    public void setup() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");

        when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findById(id);
        assertEquals("Test Role", roleEntity.get().getName());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("New Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("New Description");

        RoleEntity savedRole = roleRepository.save(roleEntity);
        assertEquals("New Role", savedRole.getName());
        assertEquals("New Description", savedRole.getDescription());
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(3, roleEntities.size());
    }

    // Add more test cases for other methods in RoleRepository

}
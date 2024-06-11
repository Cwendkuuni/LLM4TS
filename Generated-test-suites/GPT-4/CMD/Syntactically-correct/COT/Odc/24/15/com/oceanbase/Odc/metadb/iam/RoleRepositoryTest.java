package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class RoleRepositoryTest {
	
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Pageable pageable;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertSomething();
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("Manager");
        assertSomething();
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, pageable);
        assertSomething();
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Manager", 1L);
        assertSomething();
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Manager", "Admin"));
        assertSomething();
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L, 3L), pageable);
        assertSomething();
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        assertSomething();
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        assertSomething();
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
        assertSomething();
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Manager");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Manages stuff");
        roleRepository.update(roleEntity);
        assertSomething();
    }

    @After
    public void destroy() {
        verifyNoMoreInteractions(roleRepository);
    }
}
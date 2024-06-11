package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;
    private Pageable pageable;
    private List<RoleEntity> roleEntities;
    private List<Long> ids;
    private List<String> names;

    @Before
    public void setUp() {
        pageable = PageRequest.of(0,1);
        roleEntity = new RoleEntity();
        roleEntities = new ArrayList<>();
        ids = new ArrayList<>();
        names = new ArrayList<>();
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(ids);
        Mockito.verify(roleRepository, Mockito.times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("test");
        Mockito.verify(roleRepository, Mockito.times(1)).findByName("test");
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, pageable);
        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationId(1L, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("test", 1L);
        Mockito.verify(roleRepository, Mockito.times(1)).findByNameAndOrganizationId("test", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1L, names);
        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndNameIn(1L, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, ids, pageable);
        Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndRoleIds(1L, ids, pageable);
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        Mockito.verify(roleRepository, Mockito.times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        Mockito.verify(roleRepository, Mockito.times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(ids, true);
        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleIdsAndEnabled(ids, true);
    }

    @Test
    public void testUpdate() {
        roleRepository.update(roleEntity);
        Mockito.verify(roleRepository, Mockito.times(1)).update(roleEntity);
    }
}

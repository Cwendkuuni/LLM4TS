package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@JpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity()); // replace with real RoleEntity instances

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(List.of(id1, id2));

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedRole = new RoleEntity(); // replace with real RoleEntity instance

        RoleEntity actualRole = roleRepository.findByName(name).orElse(null);

        Assertions.assertEquals(expectedRole, actualRole);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = Pageable.ofSize(size).withPage(page).withSort(sort);
        Page<RoleEntity> expectedPages = Page.empty(); // replace with real Page<RoleEntity> instance

        Page<RoleEntity> actualPages = roleRepository.findByOrganizationId(organizationId, pageable);

        Assertions.assertEquals(expectedPages, actualPages);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity(); // replace with real RoleEntity instance

        Optional<RoleEntity> optionalRole = roleRepository.findByNameAndOrganizationId(name, organizationId);

        if (optionalRole.isPresent()) {
            RoleEntity actualRole = optionalRole.get();
            Assertions.assertEquals(expectedRole, actualRole);
        } else {
            Assertions.assertNull(optionalRole.get());
        }
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity()); // replace with real RoleEntity instances

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = Pageable.ofSize(size).withPage(page).withSort(sort);
        Page<RoleEntity> expectedPages = Page.empty(); // replace with real Page<RoleEntity> instance

        Page<RoleEntity> actualPages = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        Assertions.assertEquals(expectedPages, actualPages);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity()); // replace with real RoleEntity instances

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity()); // replace with real RoleEntity instances

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity()); // replace with real RoleEntity instances

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(); // replace with real RoleEntity instance
        String newName = "newName";
        Boolean newEnabled = true;
        String newDescription = "newDescription";

        Executable updateTest = () -> roleRepository.update(roleEntity);

        // Add assertions for the expected state after the update here

        updateTest.execute();
    }
}
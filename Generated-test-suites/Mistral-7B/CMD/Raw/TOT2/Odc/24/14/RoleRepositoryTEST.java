package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private static final Long TEST_ORGANIZATION_ID = 1L;
    private static final String TEST_ROLE_NAME = "testRole";

    @BeforeEach
    public void setup() {
        // Add any setup code here
    }

    @Test
    public void testFindById() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");

        RoleEntity expectedRoleEntity = roleRepository.findById(1L).orElse(null);

        RoleEntity actualRoleEntity = roleRepository.save(roleEntity);

        RoleEntity retrievedRoleEntity = roleRepository.findById(1L).orElse(null);

        assertEquals(expectedRoleEntity, retrievedRoleEntity);
    }

    @Test
    public void testFindByIdInvalidId() {
        assertThrows(NotFoundException.class, () -> {
            roleRepository.findById(-1L);
        });
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("testRole1");
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("testRole2");
        roleEntity2.setName(TEST_ROLE_NAME);
        roleRepository.save(roleEntity2);

        List<RoleEntity> expectedRoleEntities = Arrays.asList(roleEntity2);

        List<RoleEntity> actualRoleEntities = roleRepository.findByName(TEST_ROLE_NAME);

        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByNameNullName() {
        assertThrows(NullPointerException.class, () -> {
            roleRepository.findByName(null);
        });
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(TEST_ORGANIZATION_ID);
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("testRole2");
        roleEntity2.setOrganizationId(TEST_ORGANIZATION_ID);
        roleRepository.save(roleEntity2);

        Pageable pageable = PageRequest.of(0, 10);

        List<RoleEntity> expectedRoleEntities = Arrays.asList(roleEntity1, roleEntity2);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(TEST_ORGANIZATION_ID, pageable);

        assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByOrganizationIdEmpty() {
        List<RoleEntity> expectedRoleEntities = Collections.emptyList();

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(TEST_ORGANIZATION_ID, PageRequest.of(0, 10));

        assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(TEST_ORGANIZATION_ID);
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName(TEST_ROLE_NAME);
        roleEntity2.setOrganizationId(TEST_ORGANIZATION_ID);
        roleRepository.save(roleEntity2);

        Optional<RoleEntity> expectedRoleEntityOptional = Optional.of(roleEntity2);

        Optional<RoleEntity> actualRoleEntityOptional = roleRepository.findByNameAndOrganizationId(TEST_ROLE_NAME, TEST_ORGANIZATION_ID);

        assertTrue(actualRoleEntityOptional.isPresent());
        assertEquals(expectedRoleEntityOptional.get(), actualRoleEntityOptional.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameInvalidName() {
        assertThrows(NotFoundException.class, () -> {
            roleRepository.findByNameAndOrganizationId("testRoleX", TEST_ORGANIZATION_ID);
        });
    }

    @Test
    public void testFindByOrganizationIdAndNameInvalidOrganizationId() {
        assertThrows(NotFoundException.class, () -> {
            roleRepository.findByNameAndOrganizationId(TEST_ROLE_NAME, -1L);
        });
    }

    @Test
    public void testFindByOrganizationIdAndNameEmpty() {
        List<RoleEntity> expectedRoleEntities = Collections.emptyList();

        List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndName(TEST_ORGANIZATION_ID, TEST_ROLE_NAME);

        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    // Add test cases for other methods here

    @AfterEach
    public void teardown() {
        // Add any teardown code here
    }
}
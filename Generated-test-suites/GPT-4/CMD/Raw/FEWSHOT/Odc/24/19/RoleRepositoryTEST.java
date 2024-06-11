package com.oceanbase.odc.metadb.iam;

    import static org.junit.Assert.*;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;
    import org.junit.Test;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;

    public class RoleRepositoryTest {

        private RoleRepository roleRepository;
        private RoleEntity roleEntity;
        private Pageable pageable = PageRequest.of(0, 10);

        @Test
        public void testFindByIdIn() {
            List<RoleEntity> roleEntities = roleRepository.findByIdIn(Arrays.asList(roleEntity.getId()));
            assertNotNull(roleEntities);
            assertFalse(roleEntities.isEmpty());
            assertEquals(roleEntity, roleEntities.get(0));
        }

        @Test
        public void testFindByName() {
            List<RoleEntity> roleEntities = roleRepository.findByName(roleEntity.getName());
            assertNotNull(roleEntities);
            assertFalse(roleEntities.isEmpty());
            assertEquals(roleEntity, roleEntities.get(0));
        }

        @Test
        public void testFindByOrganizationId() {
            assertNotNull(roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable));
        }

        @Test
        public void testFindByNameAndOrganizationId() {
            Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId());
            assertTrue(role.isPresent());
            assertEquals(roleEntity, role.get());
        }

        @Test
        public void testFindByOrganizationIdAndNameIn() {
            List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(), Arrays.asList(roleEntity.getName()));
            assertNotNull(roleEntities);
            assertFalse(roleEntities.isEmpty());
            assertEquals(roleEntity, roleEntities.get(0));
        }

        @Test
        public void testFindByOrganizationIdAndRoleIds() {
            assertNotNull(roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), Arrays.asList(roleEntity.getId()), pageable));
        }

        @Test
        public void testFindByUserId() {
            assertNotNull(roleRepository.findByUserId(roleEntity.getUserId()));
        }

        @Test
        public void testFindByUserIdAndEnabled() {
            assertNotNull(roleRepository.findByUserIdAndEnabled(roleEntity.getUserId(), roleEntity.getEnabled()));
        }

        @Test
        public void testFindByRoleIdsAndEnabled() {
            assertNotNull(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(roleEntity.getId()), roleEntity.getEnabled()));
        }

        @Test
        public void testUpdate() {
            assertEquals(1, roleRepository.update(roleEntity));
        }
    }
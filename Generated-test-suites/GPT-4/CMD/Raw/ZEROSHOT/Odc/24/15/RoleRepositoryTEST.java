
package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        //Setup test data
        //Please modify this part as per your test data

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Administrator");
        roleEntity.setOrganizationId(1L);

        roleRepository.save(roleEntity);
    }

    @Test
    public void findByIdIn() {
        assertNotNull(roleRepository.findByIdIn(Collections.singletonList(1L)));
    }

    @Test
    public void findByName() {
        assertNotNull(roleRepository.findByName("Admin"));
    }

    @Test
    public void findByOrganizationId() {
        assertNotNull(roleRepository.findByOrganizationId(1L, PageRequest.of(0,1)));
    }

    @Test
    public void findByNameAndOrganizationId() {
        assertTrue(roleRepository.findByNameAndOrganizationId("Admin", 1L).isPresent());
    }

    @Test
    public void findByOrganizationIdAndNameIn() {
        assertNotNull(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin")));
    }

    @Test
    public void findByOrganizationIdAndRoleIds() {
        assertNotNull(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), PageRequest.of(0,1)));
    }

    @Test
    public void findByUserId() {
        // Not able to test
    }

    @Test
    public void findByUserIdAndEnabled() {
        // Not able to test
    }

    @Test
    public void findByRoleIdsAndEnabled() {
        assertNotNull(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true));
    }

    @Test
    public void update() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin2");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Administrator2");

        assertEquals(1, roleRepository.update(roleEntity));
    }
}

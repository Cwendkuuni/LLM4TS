package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        //Test goes here
    }

    @Test
    public void testFindByName() {
        //Test goes here
    }

    @Test
    public void testFindByOrganizationId() {
        //Test goes here
    }

    // Similar for other functions. 
}

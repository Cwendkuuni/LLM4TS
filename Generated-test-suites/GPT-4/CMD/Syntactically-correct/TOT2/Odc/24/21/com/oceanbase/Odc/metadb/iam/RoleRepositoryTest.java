package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testFindByIdIn() {
        //create several RoleEntity instances and save them
        //retrieve a HashSet of ids and call the findByIdIn() method
        //Check if all the RoleEntity instances are returned that correspond to the ids
    }

    @Test
    public void testFindByName() {
        //Create a few RoleEntity instances with different names and save them
        //Call findByName() method providing one of the saved entity's name
        //check if the returned Object matches the one you saved with corresponding name
    }

    // Repeat kind of similar steps for all the methods

    @Test
    public void testUpdate() {
        //Create a RoleEntity instance and save it.
        //Create another RoleEntity, then use its fields to update the saved entity with update() method call.
        //Retrieve the updated instance from the repository and check if the updated fields have changed correctly and non-updated fields remain intact.
    }
}


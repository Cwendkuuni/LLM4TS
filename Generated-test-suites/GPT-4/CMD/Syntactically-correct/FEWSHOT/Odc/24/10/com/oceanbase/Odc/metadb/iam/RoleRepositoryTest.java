
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testFindByIdIn() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByName() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByOrganizationId(){
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByNameAndOrganizationId(){
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByOrganizationIdAndNameIn(){
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByUserId() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        //populate the database with test data
        //execute and validate the test case
    }

    @Test
    public void testUpdate() {
        //populate the database with test data
        //execute and validate the test case
    }
}

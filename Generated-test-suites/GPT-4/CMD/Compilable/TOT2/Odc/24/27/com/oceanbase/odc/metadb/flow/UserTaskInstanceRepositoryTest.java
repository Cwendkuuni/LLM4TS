package com.oceanbase.odc.metadb.flow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    UserTaskInstanceRepository repository;

    @Before
    public void setup() {
        //setup goes here
    }

    @Test
    public void testFindByStatus() {
        //Test goes here
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        //Test goes here
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        //Test goes here
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        //Test goes here
    }

    @Test
    public void testFindByRoleId() {
        //Test goes here
    }

    @Test
    public void testFindByCandidateUserId() {
        //Test goes here
    }

    @Test
    public void testUpdate() {
        //Test goes here
    }

    @Test
    public void testUpdateUserTaskIdById() {
        //Test goes here
    }

    @Test
    public void testUpdateStatusById() {
        //Test goes here
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        //Test goes here
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        //Test goes here
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        //Test goes here
    }

    @Test
    public void testFindConfirmById() {
        //Test goes here
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        //Test goes here
    }
}
package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {
    @Mock
    private UserService userService;

    // mock other dependencies that will be initialized by the autowired annotation

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertNotNull("The result cannot be null", result);
    }

    // Write similar tests for all other methods by following above test case
    // For example:

    @Test
    public void testGetInstanceId2CandidateRoleIds(){
        List<Long> inputList = new ArrayList<>();
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(inputList);
        assertNotNull("The result cannot be null", result);
    }

    //...

    @After
    public void tearDown() {
        // this method will be used to release resources or clean up necessary stuffs after tests
    }
}
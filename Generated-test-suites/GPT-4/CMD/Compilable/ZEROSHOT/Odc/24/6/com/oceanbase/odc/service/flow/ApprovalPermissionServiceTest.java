package com.oceanbase.odc.service.flow;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.runner.RunWith;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalPermissionServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private ResourceRoleService resourceRoleService;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private UserResourceRoleRepository userResourceRoleRepository;

    @Autowired
    ApprovalPermissionService service;

    @Test
    public void testGetCandidates() {
        // TODO: Mock methods called by getCandidates() and give actual test input and expected results
        // when(userTaskCandidateRepository.findByApprovalInstanceId(any()).thenReturn(.........);

        // assertTrue(service.getCandidates(123L, true).equals(expectedResults));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // TODO: Mock methods called by getInstanceId2CandidateRoleIds() and give actual test input and expected results
        // when(userTaskCandidateRepository.findByApprovalInstanceIds(any()).thenReturn(.........);

        // assertTrue(service.getInstanceId2CandidateRoleIds(Arrays.asList(123L, 456L)).equals(expectedResults));
    }

    // similarly apply this for other test cases.

    @Test
    public void testGetCandidateUserIds() {
        // TODO: ...
    }

    @Test
    public void testIsApprovable() {
        // TODO: ...
    }

    @Test
    public void testSetCandidateResourceRole() {
        // TODO: ...
    }

    @Test
    public void testListApprovableExternalInstances() {
        // TODO: ...
    }

    // test each other methods like above.
}

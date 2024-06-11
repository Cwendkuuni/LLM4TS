
package com.oceanbase.odc.service.flow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ApprovalPermissionServiceTest{

  @InjectMocks
  private ApprovalPermissionService approvalPermissionService;

  @Mock
  private UserService userService;

  @Mock
  private ResourceRoleService resourceRoleService;

  @Mock
  private AuthenticationFacade authenticationFacade;

  @Mock
  private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

  @Mock
  private UserTaskInstanceRepository userTaskInstanceRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserRoleRepository userRoleRepository;
  
  //... include mocks for each autowired repository
  
  @Test
  public void testGetCandidateUserIds() {
    Set<Long> approvalInstanceIds = new HashSet<>();
    approvalInstanceIds.add(1L);
    approvalInstanceIds.add(2L);
    approvalInstanceIds.add(3L);

    approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
    
    verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(anySet());
    verify(userRoleRepository, times(1)).findByRoleIdIn(anySet());
    verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdsIn(anySet());
  }
  
  @Test
  public void testGetCandidates() {
    Set<Long> approvalInstanceIds = new HashSet<>();
    approvalInstanceIds.add(1L);

    approvalPermissionService.getCandidates(1L, true);

    verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(anyLong());
  }
  
  //... more tests for each public function in the ApprovalPermissionService class
}

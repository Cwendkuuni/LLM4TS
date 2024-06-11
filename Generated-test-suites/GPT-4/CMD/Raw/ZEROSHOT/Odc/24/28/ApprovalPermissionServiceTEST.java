
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {
    @InjectMocks
    private ApprovalPermissionService service;

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setUp() {
        //Setup before every test
    }

    @Test
    public void getCandidatesTest() {
        //Write logic to test getCandidates method
    }

    @Test
    public void getInstanceId2CandidateRoleIdsTest() {
        //Write logic to test getInstanceId2CandidateRoleIds method
    }

    @Test
    public void getInstanceId2CandidateResourceRoleIdentifierIdsTest() {
        //Write logic to test getInstanceId2CandidateResourceRoleIdentifierIds method
    }

    @Test
    public void getCandidateUserIdsTest() {
        //Write logic to test getCandidateUserIds method
    }

    @Test
    public void getCandidateRolesTest() {
        //Write logic to test getCandidateRoles method
    }

    @Test
    public void isApprovableTest() {
        //Write logic to test isApprovable method
    }

    @Test
    public void getApprovableApprovalInstancesTest() {
        //Write logic to test getApprovableApprovalInstances method
    }

    @Test
    public void setCandidateResourceRoleTest() {
        //Write logic to test setCandidateResourceRole method
    }

    @Test
    public void listApprovableExternalInstancesTest(){
        //Write logic to test listApprovableExternalInstances method
    }
    @Test
    public void getCandidatesByFlowInstanceIdsTest(){
        //Write logic to test getCandidatesByFlowInstanceIds method
    }
}

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collection;

public class RoleRepositoryTest {
    private RoleRepository roleRepoObj;

    @Before
    public void setup(){
        roleRepoObj = new MockRoleRepository();
    }

    @Test
    public void findRoleByIdInTest() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepoObj.findByIdIn(ids);
        //Assertions
    }

    @Test
    public void findByNameTest() {
        String name = "RoleName";
        List<RoleEntity> roles = roleRepoObj.findByName(name);
        //Assertions
    }

    //... similar test methods for all methods of RoleRepository Interface.

    private class MockRoleRepository implements RoleRepository {
        @Override
        public List<RoleEntity> findByIdIn(Collection<Long> ids){
            //Mock the response
        }

        @Override
        public List<RoleEntity> findByName(String name) {
            //Mock the response
        }

        //... similar mock methods for all methods of RoleRepository Interface.
    }
}
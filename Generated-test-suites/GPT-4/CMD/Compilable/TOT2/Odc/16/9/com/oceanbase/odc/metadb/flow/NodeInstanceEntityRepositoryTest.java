package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

     @Autowired 
     private NodeInstanceEntityRepository nodeRepo;

     //an example of a test
     @Test
     public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1l, 2l);
        
        List<NodeInstanceEntity> found = nodeRepo.findByIds(ids);

        assertNotNull(found);
        assertEquals(2, found.size());        
     }

     //you would write similar tests for all methods,
     //making sure that the queries are returning expected results
}
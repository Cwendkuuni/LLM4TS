import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {
private NodeInstanceEntityRepository nodeInstanceRepository;

@Before
public void setUp() {
nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
}

// Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType method
// ... (Expert 1's test case)

// Test case for findByFlowInstanceId method
// ... (Expert 1's test case)

// Test case for findByIds method
// ... (Expert 1's test case)

// Test case for deleteByFlowInstanceId method
// ... (Expert 1's test case)

// Test case for deleteByInstanceIdAndInstanceType method
// ... (Expert 1's test case)

// Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType method with null parameters
// ... (Expert 2's test case)

// Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType method with invalid parameters
// ... (Expert 2's test case)

// Test case for deleteByFlowInstanceId method with null parameter
// ... (Expert 2's test case)

// Test case for deleteByFlowInstanceId method with invalid parameter
// ... (Expert 2's test case)

// Test case for deleteByInstanceIdAndInstanceType method with null parameter
// ... (Expert 2's test case)

// Test case for deleteByInstanceIdAndInstanceType method with invalid parameter
// ... (Expert 2's test case)

// Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType method with empty result
// ... (Expert 3's test case)

// Test case for deleteByFlowInstanceId method with empty list of nodes to delete
// ... (Expert 3's test case)

// Test case for deleteByInstanceIdAndInstanceType method with empty list of nodes to delete
// ... (Expert 3's test case)
}
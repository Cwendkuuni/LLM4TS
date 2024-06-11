import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_typicalCase() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
    }

    @Test
    public void testIsSwapTableEnable_edgeCase1() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING, 
                99.9, 
                FullVerificationResult.CONSISTANT, 
                false)
        );
    }
    
    @Test
    public void testIsSwapTableEnable_edgeCase2(){
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.STOPPED, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
    }

    @Test
    public void testIsSwapTableEnable_edgeCase3(){
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
    }

    @Test
    public void testIsSwapTableEnable_exceptionCase1(){
        assertFalse(SwapTableUtil.isSwapTableEnable(
                null, 
                TaskStatus.RUNNING, 
                100.0, 
                FullVerificationResult.CONSISTEST, 
                false)
        );
    }

    @Test
    public void testIsSwapTableEnable_exceptionCase2(){
        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                null, 
                100.0, 
                FullVerificationResult.CONSISTENT, 
                false)
        );
    }
}
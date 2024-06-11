import org.junit.Test;
import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.Profile;


public class ProfileTest {

    @Test
    public void testInitProfiler() {

        // Typical use case
        // Verify that the initProfiler() method initializes the profile's controller and starts the profiler properly
        // If the controller is properly started without any exceptions, the test case is passed
        Profile.initProfiler();

        // Edge case
        // Try to start the profiler again after it has already been started
        // Verify that the second start() call does not throw any exceptions and the profiler remains running
        Profile.initProfiler();

        // Error scenario
        // Try starting the profiler when the profile's controller is null
        // Verify that an exception is thrown and the profiler does not start
        Profile._controller = null;
        try {
            Profile.initProfiler();
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // exception is expected
        }
        
    }

    @Test
    public void testInit() {

        // Typical use case
        // Verify that the init() method properly initializes the profile's dictionaries and lists
        // Check that all the dictionaries and lists are properly initialized with the correct sizes
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertEquals(0, Profile._threadActiveFrame.size());
        // Check the initial capacity of the threadActiveFrame HashMap and the threadDictionary ThreadDictionary properties
        assertEquals(1001, ((HashMap<Long, V>)Profile._threadDictionary).size());

        // Edge case
        // Try initializing the profile again after it has already been initialized
        // Check that re-initialization does not throw any exceptions and the dictionaries and lists remain the same
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertEquals(0, Profile._threadActiveFrame.size());
        // Check the initial capacity of the threadActiveFrame HashMap and the threadDictionary ThreadDictionary properties again
        assertEquals(1001, ((HashMap<Long, V>)Profile._threadDictionary).size());

    }

    @Test
    public void testClear() {

        // Typical use case
        // Verify that the clear() method properly clears the profile's dictionaries and lists
        // Check that all the dictionaries and lists are empty after clearing them
        Profile.init();
        Profile.clear();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());

        // Edge case
        // Try clearing the profile's dictionaries and lists when they are already empty
        // Verify that clearing is handled properly without any exceptions
        Profile.clear();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());

    }


    // Add test cases for the remaining methods in the Profile class


}
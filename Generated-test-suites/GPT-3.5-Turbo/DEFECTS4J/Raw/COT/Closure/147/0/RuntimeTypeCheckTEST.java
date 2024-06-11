
import static org.junit.Assert.*;

import org.junit.Test;

public class RuntimeTypeCheckTest {
	
	@Test
	public void testAddMarkers() {
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
	}

	@Test
	public void testAddChecks() {
		RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(null);
	}

	@Test
	public void testProcess() {
		RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
		runtimeTypeCheck.process(null, null);
	}

	@Test
	public void testAddMarker() {
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
	}

	@Test
	public void testFindNodeToInsertAfter() {
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
	}

	@Test
	public void testFindEnclosingConstructorDeclaration() {
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
	}

	@Test
	public void testIsClassDefiningCall() {
		RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(null);
	}

	@Test
	public void testCreateCheckTypeCallNode() {
		RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(null);
	}

	@Test
	public void testCreateCheckerNode() {
		RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(null);
	}

	@Test
	public void testAddBoilerplateCode() {
		RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null,null);
		runtimeTypeCheck.addBoilerplateCode();
	}

	@Test
	public void testGetBoilerplateCode() {
		RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null,null);
		RuntimeTypeCheck.getBoilerplateCode(null, null);
	}
	
	@Test
	public void testRuntimeTypeCheckConstructor() {
		RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null,null);
	}

	@Test
	public void testProcess() {
		RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null,null);
		runtimeTypeCheck.process(null, null);
	}

}

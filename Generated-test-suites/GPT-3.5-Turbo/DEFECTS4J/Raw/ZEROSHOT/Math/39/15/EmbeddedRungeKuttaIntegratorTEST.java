package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.nonstiff.*;

public class EmbeddedRungeKuttaIntegratorTest{
	
	public static void main(String[] args[]){
		testIntegrate();
	}
	
	public static void testIntegrate(){
		
		double[] f = {1.0};
		int[] p = {2};
		EmbeddedRungeKuttaIntegratorTest embeddedRungeKuttaIntegratorTest = new EmbeddedRungeKuttaIntegratorTest();
		ExpandableStatefulODE expandableStatefulODE = new ExpandableStatefulODE(new FirstOrderDifferentialEquations(){

			public void computeDerivatives(double t, double[] y, double[] yDot) throws MathIllegalArgumentException, MathIllegalStateException {
				// TODO Auto-generated method stub
				
			}

			public int getDimension() {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
		
		double t = 1.0;
		expandableStatefulODE.setTime(t);
		
		double[] y = {1.0, 2.0};
		expandableStatefulODE.setPrimaryState(y);
		
		double d = 1.0;
		double f1 = embeddedRungeKuttaIntegratorTest.integrate(expandableStatefulODE, d, p);
		System.out.println("Result: " + f1);
	}
	
	public double integrate(ExpandableStatefulODE state, double duration,
            int[] stages) {
		
		EmbeddedRungeKuttaIntegrator embeddedRungeKuttaIntegrator = new DormandPrince853Integrator();
		
		try{
			embeddedRungeKuttaIntegrator.integrate(state, duration);
		}
		catch (MathIllegalStateException e) {
            throw buildException(e);
        }
        catch (MathIllegalArgumentException e) {
            throw buildException(e);
        }
        return state.getTime();
	}
	
	private static RuntimeException buildException(RuntimeException exception) {
	      exception.printStackTrace();
	      return exception;
	}
	
}
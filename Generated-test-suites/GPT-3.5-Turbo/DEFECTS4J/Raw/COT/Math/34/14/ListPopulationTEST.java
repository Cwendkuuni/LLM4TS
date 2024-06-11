import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    @Test
    public void testConstructor1() {
        // Test constructor with populationLimit as 0
        try {
            ListPopulation listPopulation = new ListPopulation(0);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, e.getSpecifier());
        }
    }
    
    @Test
    public void testConstructor2() {
        // Test constructor with null chromosomes list
        try {
            ListPopulation listPopulation = new ListPopulation(null, 10);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // do nothing, expected exception
        }
    }
    
    @Test
    public void testConstructor3() {
        // Test constructor with chromosomes list size larger than populationLimit
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        try {
            ListPopulation listPopulation = new ListPopulation(chromosomes, 1);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }
    }
    
    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        
        // Test setChromosomes with null chromosomes list
        try {
            listPopulation.setChromosomes(null);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // do nothing, expected exception
        }
        
        // Test setChromosomes with chromosomes list size larger than populationLimit
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        try {
            listPopulation.setChromosomes(chromosomes);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }
    }
    
    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        
        // Test addChromosomes with list size exceed populationLimit
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        try {
            listPopulation.addChromosomes(chromosomes);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }
    }
    
    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(2);
        
        // Test addChromosome with population size exceed populationLimit
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        try {
            listPopulation.addChromosome(new Chromosome());
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }
    }
    
    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        
        // Test getFittestChromosome
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosome1.setFitness(100);
        chromosome2.setFitness(200);
        chromosome3.setFitness(50);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        Assert.assertEquals(chromosome2, fittestChromosome);
    }
    
    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(2);
        
        // Test setPopulationLimit with new limit smaller than population size
        try {
            listPopulation.setPopulationLimit(1);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // do nothing, expected exception
        }
        
        // Test setPopulationLimit with populationLimit as 0
        try {
            listPopulation.setPopulationLimit(0);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, e.getSpecifier());
        }
    }
    
    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(3);
        
        // Test getPopulationSize
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        
        int populationSize = listPopulation.getPopulationSize();
        Assert.assertEquals(chromosomes.size(), populationSize);
    }
}
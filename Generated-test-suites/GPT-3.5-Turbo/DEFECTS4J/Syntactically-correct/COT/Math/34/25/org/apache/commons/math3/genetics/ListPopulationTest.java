import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulation() {
        // Create a ListPopulation with a population limit
        ListPopulation population = new ListPopulation(50);
        assert population != null;
    }
    
    @Test(expected = NotPositiveException.class)
    public void testInvalidListPopulation() {
        // Attempt to create a ListPopulation with a non-positive population limit
        ListPopulation population = new ListPopulation(0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testChromosomesExceedPopulationLimit() {
        // Attempt to create a ListPopulation with a list of chromosomes that exceeds the population limit
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            chromosomes.add(new Chromosome());
        }
        ListPopulation population = new ListPopulation(chromosomes, 50);
    }
    
    @Test
    public void testSetChromosomes() {
        // Create a ListPopulation and set the chromosomes
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
        assert population.getChromosomes().size() == 40;
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        // Attempt to set the chromosomes of a ListPopulation to a list that exceeds the population limit
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        // Create a ListPopulation and add chromosomes
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
        assert population.getChromosomes().size() == 10;
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        // Attempt to add chromosomes to a ListPopulation that would exceed the population limit
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        // Create a ListPopulation and retrieve the chromosomes
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomes = population.getChromosomes();
        assert chromosomes != null;
    }
    
    @Test
    public void testGetChromosomeList() {
        // Create a ListPopulation and retrieve the chromosome list
        ListPopulation population = new ListPopulation(50);
        List<Chromosome> chromosomeList = population.getChromosomeList();
        assert chromosomeList != null;
    }
    
    @Test
    public void testAddChromosome() {
        // Create a ListPopulation and add a chromosome
        ListPopulation population = new ListPopulation(50);
        population.addChromosome(new Chromosome());
        assert population.getChromosomes().size() == 1;
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        // Attempt to add a chromosome to a ListPopulation that would exceed the population limit
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome() {
        // Create a ListPopulation and retrieve the fittest chromosome
        ListPopulation population = new ListPopulation(50);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assert fittestChromosome != null;
    }
    
    @Test
    public void testGetPopulationLimit() {
        // Create a ListPopulation and retrieve the population limit
        ListPopulation population = new ListPopulation(50);
        int populationLimit = population.getPopulationLimit();
        assert populationLimit == 50;
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidLimit() {
        // Attempt to set an invalid population limit for a ListPopulation
        ListPopulation population = new ListPopulation(50);
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        // Attempt to set a population limit that is smaller than the current number of chromosomes in a ListPopulation
        ListPopulation population = new ListPopulation(50);
        population.setPopulationLimit(40);
    }
    
    @Test
    public void testSetPopulationLimit() {
        // Create a ListPopulation and set the population limit
        ListPopulation population = new ListPopulation(50);
        population.setPopulationLimit(60);
        assert population.getPopulationLimit() == 60;
    }
    
    @Test
    public void testGetPopulationSize() {
        // Create a ListPopulation and retrieve the population size
        ListPopulation population = new ListPopulation(50);
        int populationSize = population.getPopulationSize();
        assert populationSize == 0;
    }
    
    @Test
    public void testToString() {
        // Create a ListPopulation and convert it to a string
        ListPopulation population = new ListPopulation(50);
        String populationString = population.toString();
        assert populationString != null;
    }
    
    @Test
    public void testIterator() {
        // Create a ListPopulation and iterate over the chromosomes
        ListPopulation population = new ListPopulation(50);
        Iterator<Chromosome> iterator = population.iterator();
        assert iterator != null;
    }
}
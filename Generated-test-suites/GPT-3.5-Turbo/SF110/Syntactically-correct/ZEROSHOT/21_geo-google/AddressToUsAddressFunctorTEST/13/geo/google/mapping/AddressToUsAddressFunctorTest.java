import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor functor;
    private GeoAddress addr;

    @Before
    public void setUp() {
        functor = AddressToUsAddressFunctor.getInstance();
        addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(countryNameCode);
        country.getCountryNameCode().addAll(countryNames);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("State");
        List<AdministrativeArea.AdministrativeAreaName> areaNames = new ArrayList<>();
        areaNames.add(areaName);
        adminArea.getAdministrativeAreaName().addAll(areaNames);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
    }

    @Test
    public void testExecute() {
        GeoUsAddress result = functor.execute(addr);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("State", result.getState());
    }

    @Test
    public void testExecute_SubAdminArea() {
        AdministrativeArea adminArea = addr.getAddressDetails().getCountry().getAdministrativeArea();
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAreaName.setContent("County");
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAreaNames = new ArrayList<>();
        subAreaNames.add(subAreaName);
        subAdminArea.getSubAdministrativeAreaName().addAll(subAreaNames);
        adminArea.setSubAdministrativeArea(subAdminArea);

        GeoUsAddress result = functor.execute(addr);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("State", result.getState());
        assertEquals("County", result.getCounty());
    }

    @Test
    public void testExecute_Locality() {
        AdministrativeArea adminArea = addr.getAddressDetails().getCountry().getAdministrativeArea();
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City");
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        localityNames.add(localityName);
        locality.getLocalityName().addAll(localityNames);
        adminArea.setLocality(locality);

        GeoUsAddress result = functor.execute(addr);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("State", result.getState());
        assertEquals("City", result.getCity());
    }

    @Test
    public void testExecute_PostalCode() {
        AdministrativeArea adminArea = addr.getAddressDetails().getCountry().getAdministrativeArea();
        Locality locality = new Locality();
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("12345");
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(postalCodeNumber);
        postalCode.getPostalCodeNumber().addAll(postalCodeNumbers);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);

        GeoUsAddress result = functor.execute(addr);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("State", result.getState());
        assertEquals("12345", result.getPostalCode());
    }

    @Test
    public void testExecute_Thoroughfare() {
        AdministrativeArea adminArea = addr.getAddressDetails().getCountry().getAdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType nameType1 = new ThoroughfareNameType();
        nameType1.setContent("Address Line 1");
        ThoroughfareNameType nameType2 = new ThoroughfareNameType();
        nameType2.setContent("Address Line 2");
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        thoroughfareNames.add(nameType1);
        thoroughfareNames.add(nameType2);
        thoroughfare.getThoroughfareName().addAll(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);

        GeoUsAddress result = functor.execute(addr);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("State", result.getState());
        assertEquals("Address Line 1", result.getAddressLine1());
        assertEquals("Address Line 2", result.getAddressLine2());
    }

}